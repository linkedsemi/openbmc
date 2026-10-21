SUMMARY = "Xuantie-900 V3.5.0 prebuilt toolchain for LS2010"
DESCRIPTION = "Unpacks the Xuantie-900 V3.5.0 release archive inside the build \
directory so that the compiler, the assembler, the linker and the target sysroot \
live next to the toolchain that OpenEmbedded builds for the host."
HOMEPAGE = "https://github.com/linkedsemi/Xuantie-900-gcc"

# Prebuilt vendor toolchain; the archive does not ship a license text.
LICENSE = "CLOSED"
LICENSE_FLAGS = "xuantie-toolchain"

XUANTIE_ARCHIVE = "Xuantie-900-gcc-linux-6.6.36-glibc-x86_64-V3.5.0-20260923.tar.gz"
XUANTIE_TOOLCHAIN_VERSION = "3.5.0"

# The archive is a single release asset, so the checksum below is the only
# provenance record: it matches the published .sha256sum file.
SRC_URI = "https://github.com/linkedsemi/Xuantie-900-gcc/releases/download/v${XUANTIE_TOOLCHAIN_VERSION}/${XUANTIE_ARCHIVE}"
SRC_URI[sha256sum] = "f15815dd83845120d669653ec209e7f34b8c3d310d81b0503b20313d21f7b410"

S = "${UNPACKDIR}/Xuantie-900-gcc-linux-6.6.36-glibc-x86_64-V3.5.0"

INHIBIT_DEFAULT_DEPS = "1"
EXCLUDE_FROM_WORLD = "1"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

# The whole archive is installed, including sysroot/: the compiler of this vendor
# toolchain finds its assembler and linker through riscv64-unknown-linux-gnu/bin,
# and external-toolchain-xuantie reads the target headers and libraries from
# sysroot/, so both halves are needed.  The tree is relocatable because
# -print-sysroot resolves as bin/../sysroot.
do_install() {
	install -d ${D}${libexecdir}/xuantie-toolchain/${XUANTIE_TOOLCHAIN_VERSION}
	cp -a ${S}/bin ${S}/include ${S}/lib ${S}/libexec ${S}/share \
		${S}/riscv64-unknown-linux-gnu ${S}/sysroot \
		${D}${libexecdir}/xuantie-toolchain/${XUANTIE_TOOLCHAIN_VERSION}/
}

FILES:${PN} = "${libexecdir}"

# libexecdir is not staged for native recipes by default.
SYSROOT_DIRS_NATIVE:append = " ${libexecdir}"

# The vendor files are prebuilt and already stripped.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_SYSROOT_STRIP = "1"
SKIP_FILEDEPS = "1"
PRIVATE_LIBS = "libgcc_s.so.1 libstdc++.so.6"
INSANE_SKIP:${PN} = "already-stripped libdir staticdev dev-so file-rdeps arch"

# Only the native variant is used: the archive provides the host side of the
# cross toolchain.  The target variant is never built.
BBCLASSEXTEND = "native"
