SUMMARY = "RISC-V Open Source Supervisor Binary Interface (LinkedSemi LS2010)"
HOMEPAGE = "https://github.com/riscv-software-src/opensbi"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING.BSD;md5=42dd9555eb177f35150cf9aa240b61e5"

PROVIDES += "opensbi"
RPROVIDES:${PN} += "opensbi"

BRANCH = "linkedsemi"
SRCREV = "25e5f84f10ea8a82aae8be7da37ea2e49a04dc7b"
SRC_URI = "git://${LINKEDSEMI_GIT}/opensbi.git;protocol=http;branch=${BRANCH}"
PV = "1.7+git"
PACKAGE_ARCH = "${MACHINE_ARCH}"

require opensbi-payloads.inc
inherit deploy

TARGET_CC_ARCH += "${LDFLAGS}"

RISCV_SBI_FW_TEXT_START ??= "0xC0000000"
EXTRA_OEMAKE += "REPRODUCIBLE=y CROSS_COMPILE=${HOST_PREFIX} ELFFLAGS="${LDFLAGS}" \
                 PLATFORM=${RISCV_SBI_PLAT} I=${D} FW_TEXT_START=${RISCV_SBI_FW_TEXT_START}"
EXTRA_OEMAKE:append = " ${@riscv_get_extra_oemake_image(d)}"
EXTRA_OEMAKE:append = " ${@riscv_get_extra_oemake_fdt(d)}"

do_compile[depends] += "${@riscv_get_do_compile_depends(d)}"

do_compile() {
	oe_runmake
}

do_install() {
	oe_runmake DESTDIR=${D} install
	rm -r ${D}/include
	rm -r ${D}/lib*
	rm -r ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/payloads
}

do_deploy() {
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_payload.* ${DEPLOYDIR}/
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_jump.* ${DEPLOYDIR}/
	install -m 755 ${D}/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.* ${DEPLOYDIR}/
}
addtask deploy before do_build after do_install

FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_jump.*"
FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_payload.*"
FILES:${PN} += "/share/opensbi/*/${RISCV_SBI_PLAT}/firmware/fw_dynamic.*"

# The vendor compiler records build paths in the debug info.
INSANE_SKIP:${PN}-dbg += "buildpaths"

COMPATIBLE_HOST = "(riscv64|riscv32).*"
