#KBRANCH ?= "dev-6.5"
#LINUX_VERSION ?= "6.5.11"
KBRANCH ?= "master"
LINUX_VERSION ?= "6.6.0"

SRCREV="ffc253263a1375a65fa6c9f62a893e9767fbebfa"

require linux-lingsi.inc

KBUILD_DEFCONFIG = "defconfig"
