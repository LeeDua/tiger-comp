package control;

/**
 * Created by qc1iu on 01/03/16.
 */

import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;

public class CommandLineParser
{
  private static final String HEADER = "The tiger compiler. Copyright (C) 2013-2015, CSS of USTC.\n\n";
  private static final String FOOTER = "\nPlease report issues at https://github.com/qc1iu/tiger-comp/issues";
  private static final String VERSION = "0.0.3";

  private Options opts;
  private String[] args;

  public CommandLineParser(String[] args)
  {
    this.args = args;
    opts = new Options();
    opts.addOption(Option.builder("o")
        .longOpt("output")
        .desc("set the name of the output file").hasArg().argName("filename")
        .build());
    opts.addOption(Option.builder("codegen")
        .desc("specific code generator to use")
        .hasArg().argName("generator")
        .build());
    opts.addOption(Option.builder("e")
        .longOpt("elab")
        .desc("dump information about elaboration")
        .hasArg()
        .argName("elabtype")
        .build());
    opts.addOption(Option.builder("O")
        .desc("optimization level")
        .hasArg()
        .argName("level")
        .build());
    opts.addOption(Option.builder("v")
        .desc("graph format")
        .hasArg()
        .argName("format")
        .build());
    opts.addOption(Option.builder("h")
        .longOpt("help")
        .desc("show this help information")
        .build());
  }

  protected void Usage()
  {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("tiger", HEADER, opts, FOOTER, true);
  }

  protected void scan() throws ParseException
  {
      CommandLine cmd = parse(this.opts, this.args);
      if (cmd.hasOption("h")) {
        Usage();
        System.exit(0);
      }
      String[] args = cmd.getArgs();
      if (args.length < 1) {
        throw new ParseException("no input file");
      }
      if (args.length > 1) {
        throw new ParseException("can only parse one file");
      }
      Control.ConCodeGen.fileName = args[0];
      if (cmd.hasOption("o")){
        Control.ConCodeGen.outputName = cmd.getOptionValue("o");
      }
      if (cmd.hasOption("codegen")) {
        String generator = cmd.getOptionValue("codegen");
        switch (generator) {
          case "C":
            Control.ConCodeGen.codegen = Control.ConCodeGen.Kind_t.C;
            break;
          case "Bytecode":
            Control.ConCodeGen.codegen = Control.ConCodeGen.Kind_t.Bytecode;
            break;
          case "Dalvik":
            Control.ConCodeGen.codegen = Control.ConCodeGen.Kind_t.Dalvik;
            break;
          case "X86":
            Control.ConCodeGen.codegen = Control.ConCodeGen.Kind_t.X86;
            break;
          default:
            throw new ParseException("expect {C|Bytecode|Dalvik|X86}");
        }
      }
  }

  private CommandLine parse(Options opts, String[] args) throws ParseException
  {
    return new DefaultParser().parse(opts, args);
  }

}
