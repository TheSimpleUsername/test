package chapter4;

/**
 * 任务类实现runnable接口，但不做具体业务操作
 * listener可以理解为一个监听器，当数据改变时调用其内的方法。
 * 此类拥有一个listener是为了不实现具体的业务逻辑，改为让listener实现业务逻辑
 * 此类的listener对象仅供自己使用，所以受保护且唯一
 * 使用枚举可以用来判断状态
 * 使用静态内部类可以用来设置属性
 *
 * 此类的所有功能皆可由一个类实现，但是分为多层多类后大大减少了单独类的代码复杂度，降低了单一类的代码量。并大大提高了代码修改时候的逻辑性 便于找bug
 *
 */
public abstract class ObserverRunnable implements Runnable {//观察者的任务

    final protected LifeCycleListener listener; //用于状态的改变提醒

    public ObserverRunnable(final LifeCycleListener listener) {
        this.listener = listener;
    }

    protected void notifyChange(final RunnableEvent event) {//数据更改后的操作
        listener.onEvent(event);
    }

    public enum RunnableState {//枚举，用于状态的判断  推荐使用枚举
        RUNNING, ERROR, DONE
    }

    public static class RunnableEvent {
        private final RunnableState state;
        private final Thread thread;
        private final Throwable cause;

        public RunnableEvent(RunnableState state, Thread thread, Throwable cause) {
            this.state = state;
            this.thread = thread;
            this.cause = cause;
        }

        public RunnableState getState() {
            return state;
        }

        public Thread getThread() {
            return thread;
        }

        public Throwable getCause() {
            return cause;
        }
    }
}
