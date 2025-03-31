grammar Little; //this indicates that this is a parser grammar, not a lexer grammar


/* BEGIN Parser Rules */
// Program Entry
program                 : PROGRAM id BEGIN program_body END ;
id                      : IDENTIFIER;
program_body            : declaration* function_declaration*;
declaration             : (string_declaration | var_declaration) SEMI;

// Global String Declaration
string_declaration      : STRING id ASIN STRINGLITERAL;

// Variable Declaration
var_declaration         : var_type id (SEP id)* ;
var_type	            : FLOAT | INT;
any_type                : var_type | VOID ;

// Function Parameter List
params                  : var_type id (SEP var_type id)* ;

// Function Declarations
function_declaration    : FUNCTION any_type id LPAR params? RPAR BEGIN func_body END;
func_body               : declaration* statement* ;

// Statement List
statement               : base_stmt | if_stmt | while_stmt;
base_stmt               : assign_stmt | read_stmt | write_stmt | return_stmt;

// Basic Statements
assign_stmt             : id ASIN expression SEMI;
read_stmt               : READ LPAR id (SEP id)* RPAR SEMI;
write_stmt              : WRITE LPAR id (SEP id)* RPAR SEMI;
return_stmt             : RETURN expression SEMI;

// Expressions
expression              : (factor addop)* factor        ;
factor                  : (postfix_expression mulop)* postfix_expression;
postfix_expression      : primary | call_expression;
call_expression         : id LPAR expression_list? RPAR;
expression_list         : expression (SEP expression)* ;
primary                 : LPAR expression RPAR | id | INTLITERAL | FLOATLITERAL;
addop                   : ADD | SUB;
mulop                   : MUL | DIV;

// Complex Statements and Conditionals
if_stmt                 : IF LPAR conditional RPAR declaration* statement* else_part? ENDIF;
else_part               : ELSE ((declaration+ | statement*) | (declaration* | statement+)) ;
conditional             : expression compop expression;
compop                  : LT | GT | EQ | NEQ | LTE | GTE;

// While Statements
while_stmt              : WHILE LPAR conditional RPAR (declaration | statement)* ENDWHILE;
/* END Parser Rules */

/* BEGIN Lexer Rules */
COMMENT                 : '--' .*? '\n' -> skip;

NEWLINE                 : '\n' -> skip;
WS                      : (' ' | '\t')+ -> skip;

PROGRAM                 : 'PROGRAM';
BEGIN                   : 'BEGIN';
END                     : 'END';
FUNCTION                : 'FUNCTION';
READ                    : 'READ';
WRITE                   : 'WRITE';
IF                      : 'IF';
ELSE                    : 'ELSE';
ENDIF                   : 'ENDIF';
WHILE                   : 'WHILE';
ENDWHILE                : 'ENDWHILE';
CONTINUE                : 'CONTINUE';
BREAK                   : 'BREAK';
RETURN                  : 'RETURN';
INT                     : 'INT';
VOID                    : 'VOID';
STRING                  : 'STRING';
FLOAT                   : 'FLOAT';

KEYWORD                 : PROGRAM | BEGIN | END | FUNCTION | READ | WRITE | IF | ELSE | ENDIF | WHILE | ENDWHILE | CONTINUE | BREAK | RETURN | INT | VOID | STRING | FLOAT;

ASIN                    : ':=';
ADD                     : '+';
SUB                     : '-';
MUL                     : '*';
DIV                     : '/';
EQ                      : '=';
NEQ                     : '!=';
LT                      : '<';
GT                      : '>';
LPAR                    : '(';
RPAR                    : ')';
SEMI                    : ';';
SEP                     : ',';
LTE                     : '<=';
GTE                     : '>=';

OPERATOR                : ASIN | ADD | SUB | MUL | DIV | EQ | NEQ | LT | GT | LTE | GTE | LPAR | RPAR | SEMI | SEP;

INTLITERAL              : [0-9]+;
FLOATLITERAL            : [0-9]* '.' [0-9]+;
STRINGLITERAL           : '"' ~[\r\n"]* '"';
IDENTIFIER              : [a-zA-Z] [a-zA-Z0-9]*;
/* END Lexer Rules */
