package chapter4;

import java.util.List;


/**
 * 本类为一个主要控制类
 * onEvent方法为当状态改变时具体的操作，concurrentQuery方法为入口，LOCK为锁
 * 流程：
 * 在此类的concurrentQuery方法中
 * 提交需要操作的任务，可使用多线程
 * 将定义的ObserverRunnable任务提交进去
 * 实现ObserverRunnable的run方法，调用该ObserverRunnable中的LifeCycleListener的实现类的onEvent方法
 */
public class ThreadLifeCycleObserver implements LifeCycleListener {

    private final Object LOCK = new Object();

    public void concurrentQuery(List<String> ids){
        if (ids == null || ids.isEmpty()){
            return;
        }
        ids.stream().forEach(id->new Thread(new ObserverRunnable(this) {
            @Override
            public void run() {
                try{
                    //该方法用于当数据改变时，提醒数据改变，将RunnableEvent此标志内部类中的值改变后，调用onEvent方法；
                    notifyChange(new RunnableEvent(RunnableState.RUNNING,Thread.currentThread(),null));

                    System.out.println("query for the id "+id);
                    Thread.sleep(1000L);
                    notifyChange(new RunnableEvent(RunnableState.DONE,Thread.currentThread(),null));

                }catch (Exception e){
                    notifyChange(new RunnableEvent(RunnableState.ERROR,Thread.currentThread(),e));
                }
            }
        },id).start());
    }

    @Override
    public void onEvent(ObserverRunnable.RunnableEvent event) {//当状态改变时调用此方法
        synchronized (LOCK){//由于该类为实现类，可能会有多线程方法，多线程调用此方法时，需要加锁。
            System.out.println("The runnable {"+event.getThread().getName()+"} data changed and state is "+event.getState());

            if(event.getCause() != null){//当事件原因不为空时，则代表并未完成或并未在工作
                System.out.println("The runnable {"+event.getThread().getName()+"} process failed ");
                event.getCause().printStackTrace();
            }
        }
    }
}
