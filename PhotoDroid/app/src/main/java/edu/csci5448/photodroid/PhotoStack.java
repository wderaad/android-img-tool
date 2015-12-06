package edu.csci5448.photodroid;
import java.util.Stack;
/**
 * Created by wjderaad on 12/2/15.
 */
public class PhotoStack {

    private Stack<Photo> photoStack;

    public PhotoStack(){
        photoStack = new Stack<Photo>();
    }

    public Photo top(){
        return photoStack.peek();
    }

    public Photo pop(){
        return photoStack.pop();
    }
    public void push(Photo photo){
        photoStack.push(photo);
    }

    public void clear(){
        photoStack.removeAllElements();
    }
}
