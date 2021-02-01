package lijie.test.interruptDemo;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
*
* 项目名称：JavaThread
* 类名称：MyTask
* 类描述：
* 创建人：liuc
* 创建时间：2018年3月19日 下午3:05:28
* 修改人：liuc
* 修改时间：2018年3月19日 下午3:05:28
* 修改备注：
* @version
*
*/
public class MyTask extends TimerTask{

	/**
	* (non-Javadoc)
	* @see TimerTask#run()
	*/
	@Override
	public void run() {
		 System.out.println("任务执行了，时间为："+new Date());
	}
}


/**
*
* 项目名称：JavaThread
* 类名称：TimerTaskRun
* 类描述：
* 创建人：liuc
* 创建时间：2018年3月19日 下午3:08:01
* 修改人：liuc
* 修改时间：2018年3月19日 下午3:08:01
* 修改备注：
* @version
*
*/
 class TimerTaskRun {
	public static void main(String[] args) {
		System.out.println("系统当前时间："+new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 10);
		Date date = calendar.getTime();
		MyTask task = new MyTask();
		Timer timer = new Timer();
		timer.schedule(task, date);
	}
}
