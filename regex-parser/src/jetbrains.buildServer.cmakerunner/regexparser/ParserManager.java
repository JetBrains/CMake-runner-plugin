
package jetbrains.buildServer.cmakerunner.regexparser;

import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * @author Vladislav.Rassokhin
 */
public class ParserManager<T extends ParserManager.Block> extends LoggerAdapter {
  @NotNull
  protected final Logger myLogger;
  @NotNull
  protected final Stack<T> myBlocksStack = new Stack<T>();

  public ParserManager(@NotNull final Logger myLogger) {
    this.myLogger = myLogger;
  }

  @NotNull
  public Logger getLogger() {
    return myLogger;
  }

  public final void log(@NotNull final String text, @NotNull final Severity severity) {
    switch (severity) {
      case INFO:
        myLogger.message(text);
        break;
      case WARN:
        myLogger.warning(text);
        break;
      case ERROR:
        myLogger.error(text);
        break;
      case BLOCK_START:
        this.blockStart(text);
        break;
      case BLOCK_FINISH:
        this.blockFinish(text);
        break;
      case BLOCK_CHANGE:
        this.blockChange(text);
        break;
      case COMPILATION_START:
        this.compilationBlockStart(text);
        break;
      case COMPILATION_FINISH:
        this.compilationBlockFinish(text);
        break;
      case COMPILATION_CHANGE:
        this.compilationBlockChange(text);
        break;
      case SPECIAL:
        if (!this.specialParse(text)) {
          myLogger.message(text);
        }
    }
  }

  protected T createBlock(@NotNull final String name) {
    //noinspection unchecked
    return (T) new Block(name);
  }

  protected T createCompilationBlock(@NotNull final String name) {
    //noinspection unchecked
    return (T) new Block(name);
  }

  @Override
  public void blockStart(@NotNull final String name) {
    final T block = createBlock(name);
    myBlocksStack.push(block);
    myLogger.blockStart(block.getName());
  }

  @Override
  public void compilationBlockStart(@NotNull final String name) {
    final T block = createCompilationBlock(name);
    myBlocksStack.push(block);
    myLogger.compilationBlockStart(name);
  }


  @Override
  public void blockFinish(@NotNull final String name) {
    if (!myBlocksStack.isEmpty()) {
      if (!myBlocksStack.peek().getName().equals(name)) return;
      myLogger.blockFinish(myBlocksStack.pop().getName());
    }
  }

  @Override
  public void compilationBlockFinish(@NotNull final String name) {
    if (!myBlocksStack.isEmpty()) {
      if (!myBlocksStack.peek().getName().equals(name)) return;
      myLogger.compilationBlockFinish(myBlocksStack.pop().getName());
    }
  }

  protected void blockChange(@NotNull final String text) {
    if (!myBlocksStack.isEmpty()) {
      myLogger.blockFinish(myBlocksStack.pop().getName());
    }
    blockStart(text);
  }

  protected void compilationBlockChange(@NotNull final String text) {
    if (!myBlocksStack.isEmpty()) {
      myLogger.compilationBlockFinish(myBlocksStack.pop().getName());
    }
    compilationBlockStart(text);
  }


  protected boolean specialParse(@NotNull final String line) {
    return false;
  }

  public void parsingError(@NotNull final String message) {
    myLogger.warning("Parsing error: " + message);
  }

  public static class Block {
    private final String myName;

    public Block(@NotNull final String name) {
      myName = name;
    }

    @NotNull
    public String getName() {
      return myName;
    }
  }
}