grammar CreateTask;

@lexer::header {
package com.ejorp.parser;
}

@header {
package com.ejorp.parser;

import com.ejorp.task.Task;
}

tasklist returns [ArrayList<Task> result]
@init {ArrayList<Task> tasks = new ArrayList<Task>();}
@after {result = tasks;}
  : ( t=task {tasks.add(t);})+;


task returns [Task result]
  : t=title {result = new Task(t);}
    ('>' (a=assignee {result.setAssigneeHandle($a.text);} | e=estimate {result.setEstimate($e.text);})+)?
    (notes {result.addNotes($notes.result);})?;

title returns [String result]
  : TASK_STRING {$result = Task.extractTitle($TASK_STRING.text);};

estimate: ESTIMATE;
assignee: '@' ID;

notes returns [ArrayList<String> result]
@init {ArrayList<String> notes = new ArrayList<String>();}
@after {result = notes;}
  : (NOTE_STRING {notes.add(Task.extractNote($NOTE_STRING.text));})+;

ESTIMATE
  : FLOAT WS* 'h'
	| FLOAT WS* '-' WS* FLOAT 'h'
	| INT WS* 'h'
	| INT WS* '-' WS* INT 'h' ;

TASK_STRING: '-' REST_OF_LINE ;
NOTE_STRING: '*' REST_OF_LINE ;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
REST_OF_LINE : ~('\n'|'\r')* '\r'? '\n'+;
