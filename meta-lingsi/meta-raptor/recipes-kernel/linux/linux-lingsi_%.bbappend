FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://bsp.cfg \
    file://0001-add-lingsi-platform.patch \
    "
