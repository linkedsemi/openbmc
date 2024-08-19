FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://multi_v7.cfg \
    file://xilinx_zynq.cfg \
    file://bsp.cfg \
    file://0001-add-lingsi-pinctrl-and-irqchip.patch \
    file://0002-add-some-driver-for-lingsi-platform.patch \
    file://0003-irq-lingsi-peri.patch \
    "

DEPENDS:append = " u-boot-mkimage-native"

KERNEL_FEATURES:remove = "cfg/fs/vfat.scc"
