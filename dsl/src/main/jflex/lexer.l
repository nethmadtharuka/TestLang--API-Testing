package com.testlang;

import java_cup.runtime.Symbol;

%%
%class TestLangScanner
%cup
%public

%%
// This rule recognizes one or more letters as a WORD token for your simple parser.
[a-zA-Z]+ { return new Symbol(sym.WORD); }

// This rule ignores whitespace.
[ \t\r\n]+ { /* Do nothing */ }

// This rule handles any other character as an error.
[^] { System.err.println("Illegal character: " + yytext()); }