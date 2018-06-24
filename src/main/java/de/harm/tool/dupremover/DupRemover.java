package de.harm.tool.dupremover;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Stream;

public class DupRemover<T> {


    public void process(IOHandler<T>[] ioHandlers, Comparator<T> topCmp) {
        T lastHandled = null;

        LinkedList<IOHandler<T>> hdList = new LinkedList<>();
        Stream.of(ioHandlers)
                .filter(cc -> !cc.isEmpty())
                .forEach(hdList::add);
        Comparator<IOHandler<T>> hdComp = (ahd, bhd) -> topCmp.compare(ahd.top(), bhd.top());

        while (!hdList.isEmpty()) {
            hdList.sort(hdComp);

            IOHandler<T> cur = hdList.getFirst();
            T top = cur.top();
            int nres = topCmp.compare(lastHandled, top);
            if (nres == 0) {
                cur.skip();
            } else if (nres > 0) {
                lastHandled = top;
                cur.use();
            } else {
                throw new IllegalArgumentException("Elements not in order: lastProcessed:" + lastHandled + " top:" +
                        top);
            }
            if (cur.isEmpty()) {
                cur.finish();
                hdList.removeFirst();
            }
        }


    }
}