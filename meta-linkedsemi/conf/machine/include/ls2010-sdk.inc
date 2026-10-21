require conf/machine/include/linkedsemi-sdk.inc
#@TYPE: Machine include
#@NAME: LinkedSemi LS2010 SoC level configuration (riscv64, C908)
DEFAULTTUNE = "riscv64"
require conf/machine/include/riscv/tune-riscv.inc

# OpenSBI (M-mode) settings
RISCV_SBI_PLAT = "generic"
RISCV_SBI_PAYLOAD = "u-boot.bin"
RISCV_SBI_FW_TEXT_START = "0xC0000000"

# U-Boot
UBOOT_ARCH = "riscv"
UBOOT_ENTRYPOINT = "0xC0400000"
UBOOT_LOADADDRESS = "0xC0400000"

# Console (uart12, 1152000 baud)
SERIAL_CONSOLES = "1152000;ttyS0"

# Provider selection
PREFERRED_PROVIDER_virtual/kernel = "linux-linkedsemi"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-linkedsemi"
PREFERRED_PROVIDER_opensbi = "opensbi-linkedsemi"
EXTRA_IMAGEDEPENDS += "opensbi-linkedsemi zero-stage-boot"
