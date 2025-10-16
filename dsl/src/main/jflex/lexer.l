package com.testlang;
%%
%public
%class Lexer
%unicode
%standalone

WORD = [a-zA-Z]+
WHITESPACE = [ \t\r\n]+

%%

{WHITESPACE}   { /* skip */ }
{WORD}         { System.out.println(yytext()); }
.              { /* ignore */ }

