# External Xuantie-900 toolchain for the target build.
#
# Enable with TCMODE = "external-xuantie" (e.g. in local.conf).  The prebuilt
# vendor toolchain provides the compiler, binutils, the C library (glibc 2.38),
# the kernel headers and the compiler runtime (libgcc/libstdc++), so none of the
# corresponding recipes are built.  external-toolchain-xuantie.bb copies the
# vendor sysroot into the recipe sysroots.
#
# Everything here is restricted to the target class on purpose: nativesdk and
# native recipes keep the toolchain that OpenEmbedded builds for the host.
#
# The toolchain understands -march=rv64imafdcv_xtheadc, which is what
# CONFIG_XUANTIE_ISA in arch/riscv/Kconfig probes for, so the kernel is built
# with the vendor ISA enabled.

require conf/distro/include/tcmode-default.inc

# The vendor toolchain is unpacked by the xuantie-toolchain-native recipe into the
# build directory, so every target recipe finds it inside its own recipe sysroot at
# the same level as the toolchain OpenEmbedded builds for the host.  No path on the
# build host is referenced here.
XUANTIE_TOOLCHAIN_VERSION ?= "3.5.0"
XUANTIE_TOOLCHAIN_DIR ?= "${STAGING_LIBEXECDIR_NATIVE}/xuantie-toolchain/${XUANTIE_TOOLCHAIN_VERSION}"
XUANTIE_TOOLCHAIN_TRIPLET ?= "riscv64-unknown-linux-gnu"
XUANTIE_TOOLCHAIN_PREFIX = "${XUANTIE_TOOLCHAIN_DIR}/bin/${XUANTIE_TOOLCHAIN_TRIPLET}-"

# Target recipes are compiled by the vendor compiler.  Keep the toolchain prefix
# bare ("riscv64-unknown-linux-gnu-", as OpenEmbedded does for the toolchain it
# builds) and put the vendor bin directory on PATH.  A bare prefix means the
# compiler command line recorded in shipped files (autoconf substitutions, build
# metadata) never carries a build host path.
HOST_PREFIX:class-target = "${XUANTIE_TOOLCHAIN_TRIPLET}-"
TARGET_PREFIX:class-target = "${XUANTIE_TOOLCHAIN_TRIPLET}-"
CROSS_COMPILE:class-target = "${HOST_PREFIX}"
PATH:prepend:class-target = "${XUANTIE_TOOLCHAIN_DIR}/bin:"

# Safety net only: with the bare prefix above, the compiler command line that
# reaches shipped files (autoconf substitutions, OpenSSL build metadata) carries
# no build host path.  Debug information can still mention the vendor tree, so map
# it onto the location the toolchain occupies in the target sysroot.
XUANTIE_TOOLCHAIN_STABLE_DIR = "${libexecdir}/xuantie-toolchain/${XUANTIE_TOOLCHAIN_VERSION}"
DEBUG_PREFIX_MAP:append:class-target = " \
    -ffile-prefix-map=${XUANTIE_TOOLCHAIN_DIR}=${XUANTIE_TOOLCHAIN_STABLE_DIR} \
    -fdebug-prefix-map=${XUANTIE_TOOLCHAIN_DIR}=${XUANTIE_TOOLCHAIN_STABLE_DIR} \
    -fmacro-prefix-map=${XUANTIE_TOOLCHAIN_DIR}=${XUANTIE_TOOLCHAIN_STABLE_DIR} \
"

# The compiler, the C library and the compiler runtime ship with the vendor
# toolchain and are never built from source.
ASSUME_PROVIDED += "virtual/compilerlibs virtual/libc virtual/libintl virtual/libiconv \
                    libxcrypt libxcrypt-native \
                    glibc glibc-initial \
                    gcc-runtime gcc-sanitizers libgfortran \
                    libgcc libgcc-initial gcc-cross-${TARGET_ARCH} \
                    binutils-cross-${TARGET_ARCH} virtual/cross-binutils virtual/${TARGET_PREFIX}binutils"

# ASSUME_PROVIDED recipes are not staged into recipe sysroots, so depend on the
# recipes that install the vendor compiler and the vendor sysroot explicitly.  The
# compiler lives in recipe-sysroot-native, hence every target recipe depends on
# xuantie-toolchain-native, not only the ones that need the target sysroot.
DEPENDS_SKIP_TOOLCHAIN = "xuantie-toolchain-native external-toolchain-xuantie linux-libc-headers"
DEPENDS:append:class-target = " xuantie-toolchain-native${@ '' if d.getVar('PN') in d.getVar('DEPENDS_SKIP_TOOLCHAIN').split() else ' external-toolchain-xuantie linux-libc-headers'}"
# kernel-yocto.bbclass wires the cross compiler into do_kernel_configme through
# virtual/cross-binutils and virtual/cross-cc.  Their providers are assumed to be
# provided above, so those task edges disappear and the kernel configuration step
# runs without a compiler in its sysroot.  Point the task at the external
# toolchain recipes explicitly.
do_kernel_configme[depends] += " xuantie-toolchain-native:do_populate_sysroot external-toolchain-xuantie:do_populate_sysroot linux-libc-headers:do_populate_sysroot"

# No SDK is generated for an external toolchain: the vendor already ships one,
# and the SDK package lists reference nativesdk recipes that are not built here.
SDK_DEPENDS = ""
TOOLCHAIN_HOST_TASK = ""
TOOLCHAIN_TARGET_TASK = ""

# The vendor sysroot already provides crypt.h and libcrypt.

# The vendor toolchain is the only C library in this build; the glibc recipe
# must not be selected as a provider for runtime dependencies.
SKIP_RECIPE[glibc] = "external-toolchain-xuantie provides the C library"
