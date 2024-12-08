#include "ds18b20.h"
#include "intrins.h"

//  定义位变量DQ，作为与18B20通信的单总线
//  目前使用P0.0，可以根据需要自行修改
sbit 		DQ = P0^0;			
#define 	DQ_H	DQ = 1;
#define 	DQ_L	DQ = 0;

//函数原型声明，可以提出为单个头文件
void reset_ds18b20(void);
void write_byte(unsigned char dat);
unsigned char read_byte(void);

/*
执行时间：约等于10*n微秒，当主频为12MHz的时候，增加一个nop
如果考虑到其他语句的执行，可以再减掉一个nop
*/
void delay_10us(unsigned char n){
	unsigned char i;
	for(i=n; i>0; --i){
		_nop_();
		//_nop_(); //主频为12MHz
	}
}


/**********************************************************************************************************
函数名称：DS18B20初始化子程序
输入参数：无
输出参数：无
函数返回：无
**********************************************************************************************************/

void reset_ds18b20(void){
  	DQ_H;          		//  DQ先置高
	delay_10us(10);     //  延时
  	DQ_L;             	//  发送复位脉冲
	delay_10us(60);     //  延时（480us - 960us)
  	DQ_H;          		//  拉高数据线
	delay_10us(24);     //  DS18B20会产生应答脉冲，持续60-240us
}


/**********************************************************************************************************
函数名称：写一字节数据到DS18B20
输入参数：数据
输出参数：无
函数返回：无
**********************************************************************************************************/
void write_byte(unsigned char dat){ 
	unsigned char i=0; 		
	for (i = 8; i > 0; i--){
		//为避免判断的时间加入到时序中，将判断放到外面
		if(dat & 0x01)
        {
			DQ_L;
			_nop_(); _nop_(); //延时 2us
            DQ_H;   //释放总线
			delay_10us(6);
        }
        else
        {
			DQ_L;
			delay_10us(6);
            DQ_H;
			_nop_(); _nop_(); //延时 2us
        }
		dat>>=1;
	}
	delay_10us(3);
}
/**********************************************************************************************************
函数名称：从ds18b20读一字节数据函数
输入参数：无
输出参数：无
函数返回：读出的字节
**********************************************************************************************************/

unsigned char read_byte(void)  			
{
	unsigned char i = 0; 		
	unsigned char dat = 0;
	bit b;
    //先读低位再读高位
    for (i = 8; i > 0; i--)  		
	{
		DQ_L;
		_nop_(); _nop_(); //延时 2us
		DQ_H;  //释放总线
		dat >>= 1;  //必须在15us内读数据, 用这个表达式的时间占位
		b= DQ;

    	if (DQ){
            dat|=0x80;
        }

		delay_10us(6);
	}
    return(dat);
}
