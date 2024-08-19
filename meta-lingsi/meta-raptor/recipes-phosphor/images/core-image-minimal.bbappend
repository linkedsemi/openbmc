IMAGE_INSTALL:append = " \
    lrzsz \
    tree \
    strace \
    dtc \
"

IMAGE_INSTALL:append:raptor-bhs = " \
    bmc-boot-done \
"
