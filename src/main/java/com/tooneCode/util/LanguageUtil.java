package com.tooneCode.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class LanguageUtil {
    public static final String PLAINTEXT = "plaintext";
    public static Map<String, String> LANGUAGE_TO_EXT = new HashMap<String, String>() {
        {
            this.put("java", "java");
            this.put("python", "py");
            this.put("javascript", "js");
            this.put("typescript", "ts");
            this.put("tsx", "tsx");
            this.put("go", "go");
            this.put("c", "c");
            this.put("cpp", "cpp");
            this.put("c++", "cpp");
            this.put("xml", "xml");
            this.put("html", "html");
            this.put("sql", "sql");
            this.put("sqlite", "sql");
            this.put("php", "php");
            this.put("kotlin", "kt");
            this.put("yaml", "yml");
            this.put("properties", "properties");
            this.put("csharp", "cs");
            this.put("c_sharp", "cs");
            this.put("css", "css");
            this.put("scss", "scss");
            this.put("vue", "vue");
        }
    };

    public LanguageUtil() {
    }

    public static String guessExtensionByMarkdownLanguage(String language) {
        return language == null ? "" : (String)LANGUAGE_TO_EXT.getOrDefault(language.toLowerCase(), language.toLowerCase());
    }

    public static String getLanguageByFilePath(String filePath) {
        String name = filePath.toLowerCase(Locale.ROOT);
        String fileName = FilenameUtils.getName(name);
        if (StringUtils.isBlank(fileName)) {
            return null;
        } else if (name.endsWith(".applescript")) {
            return "AppleScript";
        } else if (name.endsWith(".asm")) {
            return "Assembly";
        } else if (name.endsWith(".awk")) {
            return "Awk";
        } else if (!name.endsWith(".bat") && !name.endsWith(".cmd")) {
            if (!name.endsWith(".c") && !name.endsWith(".h")) {
                if (name.endsWith(".cs")) {
                    return "CSharp";
                } else if (name.endsWith(".clj")) {
                    return "Clojure";
                } else if (name.endsWith(".cmake")) {
                    return "CMake";
                } else if (name.endsWith(".coffee")) {
                    return "CoffeeScript";
                } else if (!name.endsWith(".lisp") && !name.endsWith(".cl") && !name.endsWith(".lsp")) {
                    if (!name.endsWith(".cpp") && !name.endsWith(".cxx") && !name.endsWith(".cc") && !name.endsWith(".hpp")) {
                        if (!name.endsWith(".css") && !name.endsWith(".scss")) {
                            if (name.endsWith(".cu")) {
                                return "CUDA";
                            } else if (name.endsWith(".dart")) {
                                return "Dart";
                            } else if ("dockerfile".equals(name)) {
                                return "Dockerfile";
                            } else if (!name.endsWith(".ex") && !name.endsWith(".exs")) {
                                if (name.endsWith(".erl")) {
                                    return "Erlang";
                                } else if (name.endsWith(".fs")) {
                                    return "FSharp";
                                } else if (name.endsWith(".go")) {
                                    return "Go";
                                } else if (name.endsWith(".groovy")) {
                                    return "Groovy";
                                } else if (name.endsWith(".hs")) {
                                    return "Haskell";
                                } else if (!name.endsWith(".html") && !name.endsWith(".htm") && !name.endsWith(".phtml")) {
                                    if (name.endsWith(".java")) {
                                        return "Java";
                                    } else if (name.endsWith(".jsp")) {
                                        return "JSP";
                                    } else if (!name.endsWith(".js") && !name.endsWith(".jsx")) {
                                        if (name.endsWith(".json")) {
                                            return "JSON";
                                        } else if (name.endsWith(".jl")) {
                                            return "Julia";
                                        } else if (name.endsWith(".kt")) {
                                            return "Kotlin";
                                        } else if (name.endsWith(".lua")) {
                                            return "Lua";
                                        } else if ("makefile".equals(name)) {
                                            return "Makefile";
                                        } else if (name.endsWith(".mpl")) {
                                            return "Maple";
                                        } else if (!name.endsWith(".md") && !name.endsWith(".markdown")) {
                                            if (name.endsWith(".m")) {
                                                return "Mathematica";
                                            } else if (name.endsWith(".matlab")) {
                                                return "MATLAB";
                                            } else if (!name.endsWith(".ml") && !name.endsWith(".mli")) {
                                                if (name.endsWith(".pl")) {
                                                    return "Perl";
                                                } else if (name.endsWith(".php")) {
                                                    return "PHP";
                                                } else if (name.endsWith(".ps1")) {
                                                    return "PowerShell";
                                                } else if (name.endsWith(".pl")) {
                                                    return "Prolog";
                                                } else if (name.endsWith(".proto")) {
                                                    return "ProtocolBuffer";
                                                } else if (name.endsWith(".py")) {
                                                    return "Python";
                                                } else if (name.endsWith(".r")) {
                                                    return "R";
                                                } else if (name.endsWith(".rst")) {
                                                    return "RestructuredText";
                                                } else if (name.endsWith(".rmd")) {
                                                    return "RMarkdown";
                                                } else if (name.endsWith(".rb")) {
                                                    return "Ruby";
                                                } else if (name.endsWith(".rs")) {
                                                    return "Rust";
                                                } else if (name.endsWith(".sas")) {
                                                    return "SAS";
                                                } else if (name.endsWith(".scala")) {
                                                    return "Scala";
                                                } else if (name.endsWith(".sh")) {
                                                    return "Shell";
                                                } else if (name.endsWith(".sql")) {
                                                    return "SQL";
                                                } else if (!name.endsWith(".csh") && !name.endsWith(".tcsh")) {
                                                    if (name.endsWith(".thrift")) {
                                                        return "Thrift";
                                                    } else if (!name.endsWith(".ts") && !name.endsWith(".tsx")) {
                                                        if (!name.endsWith(".vhd") && !name.endsWith(".vhdl")) {
                                                            if (name.endsWith(".vb")) {
                                                                return "VisualBasic";
                                                            } else if (name.endsWith(".y")) {
                                                                return "Yacc";
                                                            } else if (!name.endsWith(".yaml") && !name.endsWith(".yml")) {
                                                                if (name.endsWith(".xml")) {
                                                                    return "XML";
                                                                } else if (name.endsWith(".vue")) {
                                                                    return "Vue";
                                                                } else if (name.endsWith(".zig")) {
                                                                    return "Zig";
                                                                } else if (name.endsWith(".g")) {
                                                                    return "Antlr";
                                                                } else if (name.endsWith(".v")) {
                                                                    return "Verilog";
                                                                } else if (fileName.contains("dockerfile")) {
                                                                    return "Dockerfile";
                                                                } else if (fileName.contains("makefile")) {
                                                                    return "Makefile";
                                                                } else if (name.endsWith(".sparql")) {
                                                                    return "Sparql";
                                                                } else if (name.endsWith(".v")) {
                                                                    return "Verilog";
                                                                } else if (name.endsWith(".scm")) {
                                                                    return "Scheme";
                                                                } else if (name.endsWith(".pas")) {
                                                                    return "Pascal";
                                                                } else if (name.endsWith(".ftl")) {
                                                                    return "FreeMarker";
                                                                } else if (name.endsWith(".ets")) {
                                                                    return "ArkTS";
                                                                } else if (!name.endsWith(".txt") && !name.endsWith(".log")) {
                                                                    String fileExt = FilenameUtils.getExtension(name);
                                                                    return StringUtils.isBlank(fileExt) ? "plaintext" : fileExt;
                                                                } else {
                                                                    return "plaintext";
                                                                }
                                                            } else {
                                                                return "YAML";
                                                            }
                                                        } else {
                                                            return "VHDL";
                                                        }
                                                    } else {
                                                        return "TypeScript";
                                                    }
                                                } else {
                                                    return "Tcsh";
                                                }
                                            } else {
                                                return "OCaml";
                                            }
                                        } else {
                                            return "Markdown";
                                        }
                                    } else {
                                        return "JavaScript";
                                    }
                                } else {
                                    return "HTML";
                                }
                            } else {
                                return "Elixir";
                            }
                        } else {
                            return "CSS";
                        }
                    } else {
                        return "Cpp";
                    }
                } else {
                    return "CommonLisp";
                }
            } else {
                return "C";
            }
        } else {
            return "Batch";
        }
    }
}
