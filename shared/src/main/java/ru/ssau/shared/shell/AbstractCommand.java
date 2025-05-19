package ru.ssau.shared.shell;

import org.jline.terminal.Terminal;
import org.springframework.shell.component.flow.ComponentFlow;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCommand 
{
    protected static final String NULL = "NULL";
    protected final Terminal terminal;
    protected final ComponentFlow.Builder componentFlowBuilder;
}
