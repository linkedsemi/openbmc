FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:raptor-bhs = " \
    file://raptor-bhs/0001-add-lingsi-bsp.patch \
    file://raptor-bhs/bsp.cfg \
    "

SRC_URI:append:raptor-eten = " \
    file://raptor-eten/0001-add-lingsi-bsp.patch \
    file://raptor-eten/bsp.cfg \
    "
