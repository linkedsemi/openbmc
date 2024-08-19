#include <linux/init.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/uaccess.h>
#include <linux/platform_device.h>
#include <linux/ioport.h>
#include <linux/of.h>
#include <linux/of_gpio.h>
#include <linux/of_irq.h>
#include <linux/interrupt.h>

#ifndef __SELF_DBG__
#define __SELF_DBG__
#include <asm/ptrace.h>
extern void __iomem *dbg_addr;
extern bool dbg_tmp;

#define DBG_LV(lv, format,...)\
        printk(lv "dbg:%s:%d: "format"\n",__func__,__LINE__,##__VA_ARGS__);
#define DBG_LV_LINE DBG_LV(lv, "")
#define DBG_LV_HEX(lv, val) DBG_LV(lv, "%x", (val))
#define DBG_LV_HEX64(lv, val) DBG_LV(lv, "%llx", (unsigned long long)(val))
#define DBG_LV_DEC(lv, val) DBG_LV(lv, "%d", (val))
#define DBG_LV_STR(lv, val) DBG_LV(lv, "%s", (val))
#define DBG_LV_PTR(lv, val) DBG_LV(lv, "%p", (val))

#define dbg_lv_writel(lv, val, reg)\
        DBG_LV(lv, "----- mtestWADDR = 'h%8.8x; mtestWData = 'h%8.8x; dbg_api", (unsigned int)((reg) - dbg_addr), (unsigned int)(val))\
        writel((val), (reg))

#define DBG(format,...)\
        printk(KERN_CRIT "dbg:%s:%d: "format"\n",__func__,__LINE__,##__VA_ARGS__);
#define DBG_LINE DBG("")
#define DBG_HEX(val) DBG("0x%x", (val))
#define DBG_HEXL(val) DBG("0x%lx", (val))
#define DBG_HEX64(val) DBG("0x%llx", (unsigned long long)(val))
#define DBG_DEC(val) DBG("%d", (val))
#define DBG_STR(val) DBG("%s", (val))
#define DBG_PTR(val) DBG("%p", (val))

#define DBG_OPTION(format,...)\
        if(dbg_tmp){printk(KERN_CRIT "dbg:%s:%d: "format"\n",__func__,__LINE__,##__VA_ARGS__);}
#define DBG_LINE_OPTION DBG_OPTION("")
#define DBG_HEX_OPTION(val) DBG_OPTION("0x%x", (val))
#define DBG_HEXL_OPTION(val) DBG_OPTION("0x%lx", (val))
#define DBG_HEX64_OPTION(val) DBG_OPTION("0x%llx", (unsigned long long)(val))
#define DBG_DEC_OPTION(val) DBG_OPTION("%d", (val))
#define DBG_STR_OPTION(val) DBG_OPTION("%s", (val))
#define DBG_PTR_OPTION(val) DBG_OPTION("%p", (val))

#if 1
#define dbg_writel(val, reg)\
        DBG("----- mtestWADDR = 'h%8.8x; mtestWData = 'h%8.8x; dbg_api", (unsigned int)((reg) - dbg_addr), (unsigned int)(val))\
        writel((val), (reg))

#define dbg_writel_relaxed(val, reg)\
        DBG("----- mtestWADDR = 'h%8.8x; mtestWData = 'h%8.8x; dbg_api", (unsigned int)((reg) - dbg_addr), (unsigned int)(val))\
        writel_relaxed((val), (reg))
#else

#define dbg_writel(val, reg)\
        DBG("----- mtestWADDR = 'h%8.8x; mtestWData = 'h%8.8x; dbg_api", (unsigned int)((reg)), (unsigned int)(val))\
        writel((val), (reg))

#define dbg_writel_relaxed(val, reg)\
        DBG("----- mtestWADDR = 'h%8.8x; mtestWData = 'h%8.8x; dbg_api", (unsigned int)((reg)), (unsigned int)(val))\
        writel_relaxed((val), (reg))
#endif

#endif /* __SELF_DBG__ */


#define DEVICE_NAME "lingsi,home_key"

#define GPIO_BASE             896
//                                         bank       port
#define TEST_GPIO1            (GPIO_BASE + (16 * ('B' - 'A') + 6))
#define TEST_GPIO2            (GPIO_BASE + (16 * ('C' - 'A') + 6))
#define TEST_GPIO3            (GPIO_BASE + (16 * ('F' - 'A') + 6))
#define TEST_GPIO4            (GPIO_BASE + (16 * ('H' - 'A') + 6))

// SGPIO
//                                         bank       port
// #define TEST_GPIO1            (GPIO_BASE + (8 * ('B' - 'A') + 6) * 2)
// #define TEST_GPIO2            (GPIO_BASE + (8 * ('N' - 'A') + 6) * 2)
// #define TEST_GPIO3            (GPIO_BASE + (8 * ('O' - 'A') + 6) * 2)
// #define TEST_GPIO4            (GPIO_BASE + (8 * ('P' - 'A') + 6) * 2)
// #define TEST_GPIO1            0
// #define TEST_GPIO2            30
// #define TEST_GPIO3            122
// #define TEST_GPIO4            1010
// #define TEST_GPIO3            962
// #define TEST_GPIO4            978
// #define TEST_GPIO3            1009
// #define TEST_GPIO4            1017
// #define TEST_GPIO5            1017
int home_gpio_idx1=0;  //home键的gpio编号
int home_inter_idx1=0; //home键gpio对应的中断号
int home_gpio_idx2=0;  //home键的gpio编号
int home_inter_idx2=0; //home键gpio对应的中断号
int home_gpio_idx3=0;  //home键的gpio编号
int home_inter_idx3=0; //home键gpio对应的中断号
int home_gpio_idx4=0;  //home键的gpio编号
int home_inter_idx4=0; //home键gpio对应的中断号

#ifdef TEST_GPIO1
static irqreturn_t home1_interrupt(int irq, void *dev_id) {
        printk("%s(%d)\n", __FUNCTION__, __LINE__);
        printk("HOME1 KEY INTERRUPT!\n");
DBG_LINE
        return IRQ_HANDLED;
}
#endif

#ifdef TEST_GPIO2
static irqreturn_t home2_interrupt(int irq, void *dev_id) {
        printk("%s(%d)\n", __FUNCTION__, __LINE__);
        printk("HOME2 KEY INTERRUPT!\n");
DBG_LINE
        return IRQ_HANDLED;
}
#endif
#ifdef TEST_GPIO3
static irqreturn_t home3_interrupt(int irq, void *dev_id) {
        printk("%s(%d)\n", __FUNCTION__, __LINE__);
        printk("HOME3 KEY INTERRUPT!\n");
DBG_LINE
        return IRQ_HANDLED;
}
#endif
#ifdef TEST_GPIO4
static irqreturn_t home4_interrupt(int irq, void *dev_id) {
        printk("%s(%d)\n", __FUNCTION__, __LINE__);
        printk("HOME4 KEY INTERRUPT!\n");
DBG_LINE
        return IRQ_HANDLED;
}
#endif

int inline home_request(int home_gpio_idx, int *home_inter_idx, irqreturn_t home_interrupt(int, void *), char *name,struct platform_device *dev) {
    int ret;
DBG_DEC(home_gpio_idx)
    ret = gpio_request(home_gpio_idx,"home");  
    if( ret != 0){
        printk("%s gpio request failed\n", name);
        return -1;
    }
    gpio_direction_input(home_gpio_idx);  
    *home_inter_idx = gpio_to_irq(home_gpio_idx);  
DBG_DEC(*home_inter_idx)

    ret = request_irq(*home_inter_idx, home_interrupt, IRQF_TRIGGER_FALLING, name, dev);
    // ret = request_irq(*home_inter_idx, home_interrupt, IRQF_TRIGGER_RISING, name, dev);
    if(ret < 0){
        printk("%s request interrupt failed, IRQ=%d,ret=%d\n", name, *home_inter_idx, ret);
        gpio_free(home_gpio_idx);
        return -1;
    }
DBG_LINE
    return home_gpio_idx;
}

int drProbe(struct platform_device *dev){
DBG_LINE
#ifdef TEST_GPIO1
    home_gpio_idx1 = home_request(TEST_GPIO1, &home_inter_idx1, home1_interrupt, "home_key1", dev);
#endif
DBG_LINE
#ifdef TEST_GPIO2
    home_gpio_idx2 = home_request(TEST_GPIO2, &home_inter_idx2, home2_interrupt, "home_key2", dev);
#endif
DBG_LINE
#ifdef TEST_GPIO3
    home_gpio_idx3 = home_request(TEST_GPIO3, &home_inter_idx3, home3_interrupt, "home_key3", dev);
#endif
DBG_LINE
#ifdef TEST_GPIO4
    home_gpio_idx4 = home_request(TEST_GPIO4, &home_inter_idx4, home4_interrupt, "home_key4", dev);
#endif
DBG_LINE
    return 0;
}

int drRemove(struct platform_device *dev){
DBG_LINE

#ifdef TEST_GPIO1
    free_irq(home_inter_idx1, dev);
    gpio_free(home_gpio_idx1);
#endif
#ifdef TEST_GPIO2
    free_irq(home_inter_idx2, dev);
    gpio_free(home_gpio_idx2);
#endif
#ifdef TEST_GPIO3
    free_irq(home_inter_idx3, dev);
    gpio_free(home_gpio_idx3);
#endif
#ifdef TEST_GPIO4
    free_irq(home_inter_idx4, dev);
    gpio_free(home_gpio_idx4);
#endif
    printk("driver remove\n");
    return 0;
}

static const struct of_device_id of_interr_match[] = {
    {.compatible = DEVICE_NAME},
    {},
};

static struct platform_driver pdrv = {
    .probe = drProbe,
    .remove = drRemove,
    .driver = {
        .name = DEVICE_NAME,
        .owner = THIS_MODULE,
        .of_match_table = of_interr_match,
    }
};

static int driver_init_interr(void){
    int ret=0;
    ret = platform_driver_register(&pdrv);
    if(ret < 0){
        printk("platform driver regist failed\n");
        return -1;
    }
    return 0;
}

static void driver_exit_interr(void){
   platform_driver_unregister(&pdrv);
   printk("platform driver exit!\n");
}

module_init(driver_init_interr);
module_exit(driver_exit_interr);

MODULE_LICENSE("Dual BSD/GPL");
