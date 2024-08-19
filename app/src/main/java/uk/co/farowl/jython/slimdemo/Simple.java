package uk.co.farowl.jython.slimdemo;

import org.python.util.PythonInterpreter;

public class Simple {

    public static void main(String[] args) {
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("print 6*7");
    }

}
