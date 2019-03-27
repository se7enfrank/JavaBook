package com.jit.one;
class Info{
	private String pd1;
	private String pd2;
	public  synchronized void set(String pd1,String pd2)
	{//此处用synchronized修饰的方法锁住的是方法里边的代码还是调用这个方法的对象
		//这个方法能不能执行到一半中途被抢占线程而暂停。
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.pd1=pd1;
		this.pd2=pd2;
	}
	public synchronized void get()
	{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.pd1+"  "+this.pd2);
	}	
}
class Productor implements Runnable
{
	private Info info;
	public Productor(Info info)
	{
		this.info=info;
	}
	@Override
	public void run() {
		for(int i=0;i<10;i++)
		{
			//当线程A运行到此处时，有可能被线程B抢占而停止运行，这时候底下的赋值语句并没有执行。
			if(i%2==0)
			{
				this.info.set("可爱的猫","美短");
			}else
			{
				this.info.set("可爱的狗","哈士奇");
			}
		}
	}
	
}
class Customer implements Runnable{
	private Info info;
	public Customer(Info info)
	{
		this.info=info;
	}
	@Override
	public void run() {
		//线程B执行完一次循环之后，由于线程A没有执行完而没有赋值，导致线程B运行的结果有可能是NULL值，但是这个程序代码我允许了很多次从来没有得到过NULL值。
		for(int i=0;i<10;i++)
		{
			this.info.get();
		}
	}
}
public class Test1 {
	
	public static void main(String[] args) {
		Info info=new Info();
		new Thread(new Productor(info),"线程A").start();
		new Thread(new Customer(info),"线程B").start();
	}
}
