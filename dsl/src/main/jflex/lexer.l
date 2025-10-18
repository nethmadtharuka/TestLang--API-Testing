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

/* String literals */
\"([^\\\"]|\\.)*\"  {
    String str = yytext();
    str = str.substring(1, str.length() - 1);
    return symbol(sym.STRING, str);
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