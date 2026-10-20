SUMMARY = "XuanTie zero stage boot (ZSBL) for LinkedSemi LS2010"
HOMEPAGE = "https://github.com/XUANTIE-RV/zero_stage_boot"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d229da563da18fe5d58cd95a6467d584"

BRANCH = "linkedsemi"
SRCREV = "a2811cd1600c7ee316b179ff5a819c214f9bd943"
SRC_URI = "git://${LINKEDSEMI_GIT}/zero_stage_boot.git;protocol=http;branch=${BRANCH}"
PV = "1.4.0+git${SRCPV}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

do_compile() {
	oe_runmake CROSS_COMPILE=${TARGET_PREFIX}
}

do_install() {
	install -Dm644 ${B}/zero_stage_boot.bin ${D}/boot/zero_stage_boot.bin
}

do_deploy() {
	install -m 644 ${B}/zero_stage_boot.bin ${DEPLOYDIR}/zero_stage_boot.bin
}
addtask deploy before do_build after do_compile

FILES:${PN} += "/boot/zero_stage_boot.bin"
COMPATIBLE_HOST = "(riscv64|riscv32).*"
