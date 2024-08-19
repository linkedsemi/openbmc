SUMMARY = "A service to print /dev tree on startup"
DESCRIPTION = "This recipe installs a systemd service that prints the /dev directory tree after the system boots."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://../COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI= " file://test_systemd.sh \
           file://test_systemd.service \
           file://COPYING \
           "

inherit systemd
RDEPENDS:${PN} += "bash"
RPROVIDES:${PN} = "test_systemd"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/test_systemd.sh ${D}/${bindir}

    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/test_systemd.service \
        ${D}${systemd_unitdir}/system

    cp -r ${WORKDIR}/COPYING ${S}
}

SYSTEMD_SERVICE:${PN}= " test_systemd.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
FILES:${PN} += "${bindir}/test_systemd.sh"
RDEPENDS:${PN} = " tree bash"
