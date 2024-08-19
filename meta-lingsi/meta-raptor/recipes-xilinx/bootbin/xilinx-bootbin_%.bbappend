
DEPENDS += "zip-native"

do_deploy:append() {
    zip -j ${DEPLOYDIR}/boot.bin.zip ${DEPLOYDIR}/boot.bin
}
