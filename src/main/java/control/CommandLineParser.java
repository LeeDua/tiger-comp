package control;

/**
 * Created by qc1iu on 01/03/16.
 */

import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;

public class CommandLineParser
{
  private static final String VERSION = "v0.0.3";
  private static final String HEADER =
      "The tiger compiler " + VERSION +
          ". Copyright (C) 2013-2016, CSS of USTC.\n\n";
  private static final String FOOTER =
      "\nPlease report issues at https://github.com/qc1iu/tiger-comp/issues";

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

  private void Usage()
  {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("tiger", HEADER, opts, FOOTER, true);
    System.out.println();
  }

  public void scan() throws ParseException
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
    if (cmd.hasOption("o")) {
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
    if (cmd.hasOption("v")) {
      String format = cmd.getOptionValue("v");
      switch (format) {
        case "bmp":
          Control.visualize = Control.Visualize_Kind_t.Bmp;
          break;
        case "pdf":
          Control.visualize = Control.Visualize_Kind_t.Pdf;
          break;
        case "svg":
          Control.visualize = Control.Visualize_Kind_t.Svg;
          break;
        case "jpg":
          Control.visualize = Control.Visualize_Kind_t.Jpg;
          break;
        default:
          throw new ParseException("expect {bmp|pdf|svg|jpg}");
      }
    }
  }

  private CommandLine parse(Options opts, String[] args) throws ParseException
  {
    return new DefaultParser().parse(opts, args);
  }

}
