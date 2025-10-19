package com.testlang;

import java_cup.runtime.Symbol;

%%
%class TestLangScanner
%cup
%public
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

%%

/* Keywords */
"config"    { return symbol(sym.CONFIG); }
"let"       { return symbol(sym.LET); }
"test"      { return symbol(sym.TEST); }
"get"       { return symbol(sym.GET); }
"post"      { return symbol(sym.POST); }
"put"       { return symbol(sym.PUT); }
"delete"    { return symbol(sym.DELETE); }
"expect"    { return symbol(sym.EXPECT); }
"status"    { return symbol(sym.STATUS); }
"header"    { return symbol(sym.HEADER); }
"body"      { return symbol(sym.BODY); }
"contains"  { return symbol(sym.CONTAINS); }
"base_url"  { return symbol(sym.BASE_URL); }

/* Symbols */
"="         { return symbol(sym.EQ); }
"{"         { return symbol(sym.LBRACE); }
"}"         { return symbol(sym.RBRACE); }
";"         { return symbol(sym.SEMI); }

/* String literals - properly handle escape sequences */
\"(\\.|[^\\\"])*\"  {
    String str = yytext();
    // Remove the surrounding quotes
    str = str.substring(1, str.length() - 1);
    // Process escape sequences properly
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == '\\' && i + 1 < str.length()) {
            char next = str.charAt(i + 1);
            switch (next) {
                case 'n':
                    result.append('\n');
                    i++;
                    break;
                case 't':
                    result.append('\t');
                    i++;
                    break;
                case 'r':
                    result.append('\r');
                    i++;
                    break;
                case '\\':
                    result.append('\\');
                    i++;
                    break;
                case '"':
                    result.append('"');
                    i++;
                    break;
                default:
                    // Keep the backslash if it's not a recognized escape
                    result.append(str.charAt(i));
            }
        } else {
            result.append(str.charAt(i));
        }
    }
    return symbol(sym.STRING, result.toString());
}

/* Numbers */
[0-9]+(\.[0-9]+)?  { return symbol(sym.NUMBER, yytext()); }

/* Identifiers (must come after keywords) */
[a-zA-Z_][a-zA-Z0-9_]* { return symbol(sym.IDENT, yytext()); }

/* Whitespace */
[ \t\r\n]+  { /* Ignore */ }

/* Comments */
"//".*      { /* Ignore */ }

/* Error */
.           { System.err.println("Illegal character: '" + yytext() + "'"); }