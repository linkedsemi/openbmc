DESCRIPTION = "Linux kernel for LinkedSemi LS2010 BMC SoC"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PROVIDES += "virtual/kernel"
# kernel-yocto must run alldefconfig, otherwise the implied symbols of the
# board defconfig are not re-derived and drivers silently drop out (observed:
# CONFIG_TTY stays unset and CONFIG_SERIAL_8250 disappears from .config).
KCONFIG_MODE = "--alldefconfig"
KSRC ?= "git://${LINKEDSEMI_GIT}/linux.git;protocol=http;branch=${KBRANCH}"
SRC_URI += "${KSRC}"

# The board defconfig and DTS files live in the kernel tree itself
# (arch/riscv/configs/linkedsemi_ls2010_defconfig,
# arch/riscv/boot/dts/linkedsemi/); KBUILD_DEFCONFIG comes from the machine
# configuration.

LINUX_VERSION_EXTENSION ?= ""
PV = "${LINUX_VERSION}+git${SRCPV}"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# Custom kernel tree carries no yocto .scc metadata
KERNEL_FEATURES:remove:riscv64 = "${KERNEL_FEATURES_RISCV}"

# Linux 6.18 installs modules.builtin.ranges, unknown to the wrynose oe-core
# kernel packaging rules
FILES:${KERNEL_PACKAGE_NAME}-modules:append = " ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.ranges"

ERROR_QA:remove = "buildpaths"
WARN_QA:append = "buildpaths"
