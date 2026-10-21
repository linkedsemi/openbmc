SUMMARY = "Xuantie-900 external C library, headers and objects for LS2010"
DESCRIPTION = "Installs the glibc headers, start files and shared objects shipped \
with the Xuantie-900 V3.5.0 toolchain so that recipes can link against them when \
TCMODE is set to external-xuantie."
# Prebuilt vendor toolchain runtime; not redistributable as source.
LICENSE = "CLOSED"
LICENSE_FLAGS = "xuantie-toolchain"

# The archive is unpacked by xuantie-toolchain-native into the build directory, so
# this recipe reads the sysroot from the staged native component.  That keeps the
# vendor toolchain at the same level as the toolchain OpenEmbedded builds itself.
INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "xuantie-toolchain-native"
EXCLUDE_FROM_WORLD = "1"

XUANTIE_TOOLCHAIN ?= "${STAGING_LIBEXECDIR_NATIVE}/xuantie-toolchain/3.5.0"
XUANTIE_TOOLCHAIN_SYSROOT ?= "${XUANTIE_TOOLCHAIN}/sysroot"
XUANTIE_TRIPLET ?= "riscv64-unknown-linux-gnu"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_patch[noexec] = "1"

COMPATIBLE_HOST = "(riscv64|riscv32).*"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Flat layout: everything the compiler looks for under its ${sysroot} search
# paths lives in /lib64/lp64d and /lib.  No /usr/lib64 or /usr/lib copies are
# installed, so no shared object is shipped twice.
xuantie_untar() {
	src="$1"
	dst="$2"
	shift 2
	install -d "${dst}"
	tar -C "${src}" -cf - --exclude='.debug' --exclude='*/.debug' "$@" . \
		| tar -C "${dst}" --no-same-owner --no-same-permissions -xf -
}

do_install() {
	install -d ${D}${includedir} ${D}${libdir} ${D}${base_libdir}

	xuantie_untar ${XUANTIE_TOOLCHAIN_SYSROOT}/usr/include ${D}${includedir}

	# The vendor kernel headers are 6.6.36 while the kernel of this board is
	# 6.18, so drop them and let linux-libc-headers provide matching ones.
	for hdr in asm asm-generic drm linux misc mtd rdma sound video xen; do
		rm -rf ${D}${includedir}/$hdr
	done
	# The scsi headers that stay are C library headers, not kernel uapi: glibc
	# installs them from sysdeps/unix/sysv/linux/scsi/, linux-libc-headers 6.18
	# does not export them, and util-linux includes them unconditionally.
	# Because glibc is ASSUME_PROVIDED in this build the glibc recipe never runs,
	# so the only remaining source for these three files is this vendor sysroot.
	rm -rf ${D}${includedir}/scsi/fc
	rm -f ${D}${includedir}/scsi/scsi_bsg_fc.h \
		${D}${includedir}/scsi/scsi_bsg_mpi3mr.h \
		${D}${includedir}/scsi/scsi_bsg_ufs.h \
		${D}${includedir}/scsi/scsi_netlink.h \
		${D}${includedir}/scsi/scsi_netlink_fc.h

	# One canonical library directory.  The compiler looks for its start files
	# and linker scripts in ${sysroot}/usr/lib64/lp64d, the loader looks for the
	# shared objects in the same place once /lib64 is a symlink to /usr/lib64.
	xuantie_untar ${XUANTIE_TOOLCHAIN_SYSROOT}/lib64/lp64d ${D}/usr/lib64/lp64d
	xuantie_untar ${XUANTIE_TOOLCHAIN_SYSROOT}/usr/lib64/lp64d ${D}/usr/lib64/lp64d \
		--skip-old-files

	# The vendor linker scripts reference /lib64/lp64d and the loader with
	# absolute paths.  Those directories do not exist inside a recipe sysroot,
	# so make the references relative to the linker search paths.
	for script in ${D}/usr/lib64/lp64d/*; do
		if [ -f "$script" ] && [ ! -L "$script" ] && \
		   head -n 1 "$script" | grep -q 'GNU ld script'; then
			sed -i -e 's#\([ (]\)/lib64/lp64d/#\1#g' -e 's#\([ (]\)/lib/ld-#\1ld-#g' "$script"
		fi
	done

	# Development symlinks.  Without libpthread.so the linker silently picks
	# the static libpthread.a, which does not work with a dynamic libc.
	for so in ${D}/usr/lib64/lp64d/lib*.so.[0-9]*; do
		[ -e "$so" ] || continue
		name=$(basename "$so")
		link=${name%%.so*}.so
		dev=${D}/usr/lib64/lp64d/$link
		# Dangling symlinks point into the vendor /lib64 layout, replace them.
		if [ -L "$dev" ] && [ ! -e "$dev" ]; then
			rm -f "$dev"
		fi
		[ -e "$dev" ] || ln -s "$name" "$dev"
	done

	# This toolchain ships glibc 2.38, where libpthread, libdl and librt are
	# part of libc.so.6 and only empty compatibility stubs remain.  Add the
	# stubs to the libc.so group so that a binary carrying a legacy
	# DT_NEEDED entry for them still resolves.
	sed -i -e 's#GROUP ( libc.so.6 #GROUP ( libc.so.6 libpthread.so.0 libdl.so.2 librt.so.1 #' \
		${D}/usr/lib64/lp64d/libc.so

	# The dynamic linker is looked up as /lib/ld-linux-riscv64-lp64d.so.1.  The
	# vendor sysroot also ships riscv32 loaders for the ilp32 ABIs, which cannot
	# be installed in a riscv64 image.
	for loader in ${XUANTIE_TOOLCHAIN_SYSROOT}/lib/ld-linux-riscv64-*; do
		cp -a --no-preserve=ownership "${loader}" ${D}${base_libdir}/
	done

	# usrmerge layout: /lib and /lib64 point at the /usr counterparts.
	ln -sf usr/lib ${D}/lib
	ln -sf usr/lib64 ${D}/lib64

	find ${D} -type d -name .debug -prune -exec rm -rf {} +

	# glibc 2.38 no longer ships crypt.h or libcrypt, and this vendor build
	# does not bundle libxcrypt either, so the removals below only guard
	# against a future toolchain that starts shipping them again.
	rm -f ${D}${includedir}/crypt.h
	rm -f ${D}/usr/lib64/lp64d/libcrypt.so ${D}/usr/lib64/lp64d/libcrypt.a

	# No prefixed aliases are installed here: the compiler now lives inside the
	# recipe sysroot machinery, so symlinks to it would point into TMPDIR and
	# would be dangling inside the target image.  Recipes use ${CC}/${CXX}.
}

# The vendor objects are prebuilt and partially stripped; do not touch them.
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

# The vendor libraries live in /usr/lib64, which is not staged by default.
SYSROOT_DIRS += "/usr/lib64 /lib /lib64"

RPROVIDES:${PN} += "libc6 libc6-dev glibc glibc-dev \
                    libgcc libgcc-dev libstdc++ libstdc++-dev"

INSANE_SKIP:${PN} += "dev-so staticdev already-stripped debug-files libdir"

FILES:${PN} = "/"
