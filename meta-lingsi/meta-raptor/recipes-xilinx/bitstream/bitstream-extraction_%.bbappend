FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# BITSTREAM_PATH_EXT = "ax7450_ec_20240409_i2c_3.bit"
# BITSTREAM_PATH_EXT = "espi_debug_20240612_0.bit"
# BITSTREAM_PATH_EXT = "rgmii_10m_20240625_3.bit"
# BITSTREAM_PATH_EXT = "rgmii_10m_nopull.bit"
# BITSTREAM_PATH_EXT = "pcie_sgpio_20240625.bit"
# BITSTREAM_PATH_EXT = "run_bmc_v2.bit"
# BITSTREAM_PATH_EXT = "run_bmc_spi.bit"
# BITSTREAM_PATH_EXT = "lite_bmc_gpio_debug_20240703_1.bit"
# BITSTREAM_PATH_EXT = "run_bmc_v2_I2C_20240711_1.bit"

# BITSTREAM_PATH_EXT = "run_bmc_v2_I2C_20240712_0.bit"
# BITSTREAM_PATH_EXT = "run_bmc_v2_20240813.bit"

BITSTREAM_PATH_EXT = "run_bmc_v2_20240815_2_update_sgpio.bit"

BITSTREAM_PATH_EXT[doc] = "bitstream file name"

SRC_URI:append := " ${@'file://%s' % d.getVar('BITSTREAM_PATH_EXT') if (d.getVar('BITSTREAM_PATH_EXT')) else ''}"

do_install() {
    if [ -f ${WORKDIR}/${BITSTREAM_PATH_EXT} ]; then
        install -d ${D}/boot/bitstream/
        install -Dm 0644 ${WORKDIR}/${BITSTREAM_PATH_EXT} ${D}/boot/bitstream/${BITSTREAM_BASE_NAME}.bit
        ln -sf ${BITSTREAM_BASE_NAME}.bit ${D}/boot/bitstream/${BITSTREAM_NAME}-${MACHINE}.bit
    elif [ -e ${XSCTH_WS}/${XSCTH_PROJ}_hwproj/*.bit ]; then
        install -d ${D}/boot/bitstream/
        install -Dm 0644 ${XSCTH_WS}/${XSCTH_PROJ}_hwproj/*.bit ${D}/boot/bitstream/${BITSTREAM_BASE_NAME}.bit
        ln -sf ${BITSTREAM_BASE_NAME}.bit ${D}/boot/bitstream/${BITSTREAM_NAME}-${MACHINE}.bit
    else
    install -d ${D}/boot/bitstream/
        touch ${D}/boot/bitstream/${BITSTREAM_NAME}-${MACHINE}.bit
    fi
}

do_deploy() {
    if [ -f ${WORKDIR}/${BITSTREAM_PATH_EXT} ]; then
        install -Dm 0644 ${WORKDIR}/${BITSTREAM_PATH_EXT} ${DEPLOYDIR}/${BITSTREAM_BASE_NAME}.bit
        ln -sf ${BITSTREAM_BASE_NAME}.bit ${DEPLOYDIR}/${BITSTREAM_NAME}-${MACHINE}.bit
    elif [ -e ${XSCTH_WS}/${XSCTH_PROJ}_hwproj/*.bit ]; then
        install -Dm 0644 ${XSCTH_WS}/${XSCTH_PROJ}_hwproj/*.bit ${DEPLOYDIR}/${BITSTREAM_BASE_NAME}.bit
        ln -sf ${BITSTREAM_BASE_NAME}.bit ${DEPLOYDIR}/${BITSTREAM_NAME}-${MACHINE}.bit
    else
        touch ${DEPLOYDIR}/${BITSTREAM_NAME}-${MACHINE}.bit
    fi
}
