OBMC_IMAGE_EXTRA_INSTALL:append = " \
    tree \
    strace \
    ethtool \
    i2c-tools \
    dtc \
"

OBMC_IMAGE_EXTRA_INSTALL:append:raptor-bhs = " \
    bmc-boot-done \
"
