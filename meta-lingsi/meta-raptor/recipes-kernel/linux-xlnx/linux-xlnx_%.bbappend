FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://bsp-${MACHINE}.cfg \
    file://0001-remove-unused-variable.patch \
    file://0002-add-lingsi-pinctrl-and-irqchip.patch \
    "

DEPENDS:append = " u-boot-mkimage-native"
