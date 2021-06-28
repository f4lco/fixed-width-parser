package com.joutvhu.fixedwidth.parser.check;

import java.util.List;

public interface ModelInspector {

    List<Checker.Entry> inspect(Class<?> model);
}
