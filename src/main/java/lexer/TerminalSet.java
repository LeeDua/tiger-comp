package lexer;

import java.util.HashSet;

/**
 * Created by qc1iu on 2/23/16.
 */
@Deprecated
public class TerminalSet {
  public HashSet<Character> terminals;

  public TerminalSet() {
    terminals = new HashSet<>();
    terminals.add('/');
    terminals.add(' ');
    terminals.add('\n');
    terminals.add('\t');
    terminals.add('\r');
    terminals.add('+');
    terminals.add(',');
    terminals.add('.');
    terminals.add('{');
    terminals.add('[');
    terminals.add('(');
    terminals.add('}');
    terminals.add(']');
    terminals.add(')');
    terminals.add('<');
    terminals.add('!');
    terminals.add(';');
    terminals.add('-');
    terminals.add('*');
    terminals.add('&');
  }

  public boolean isTerminal(int c) {
    if (c == -1) {
      return true;
    }
    return this.terminals.contains((char) c);
  }
}
