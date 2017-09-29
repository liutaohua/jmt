package cc.tpark.session.protocol;

public interface IMsgElement {
    void accept(IVistor vistor);
}
