package chapter4;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private List<Observer> observers = new ArrayList<>();//观察者队列

    private int state;//状态

    public int getState(){
        return this.state;
    }

    public void setState(int state){
        if(state == this.state){
            return;
        }
        this.state = state;
        notifyAllObserver();
    }

    public void attach(Observer observer){//将观察者添加到队列中
        observers.add(observer);
    }

    private void notifyAllObserver(){//状态改变时所有观察者数据进行更新
        observers.stream().forEach(Observer::update);
    }
}
