# SPAN - Stochastic Protocol ANalyzer

SPAN is a formal verification engine for model checking indistinguishability (trace equivalence) and state-based safety properties of randomized security protocols in the symbolic model.  

Information regarding threat model and the process calculus can be found in the following papers: 

- Matthew S. Bauer, Rohit Chadha, A. Prasad Sistla and Mahesh Viswanathan. Model checking indistinguishability in randomized security protocols. Computer Aided Verification (CAV), 2018. 

- Rohit Chadha, A. Prasad Sistla and Mahesh Viswanathan. [Verification of randomized security protocols](http://ieeexplore.ieee.org/document/8005126/?reload=true). Logic in Computer Science (LICS), 2017. 

- Matthew S. Bauer, Rohit Chada and Mahesh Viswanathan. [Modular verification of protocol equivalence in the presence of randomness](https://link.springer.com/chapter/10.1007/978-3-319-66402-6_12). European Symposium on Research in Computer Security (ESORICS), 2017. 

- Matthew S. Bauer, Rohit Chada and Mahesh Viswanathan. [Composing protocols with randomized actions](https://link.springer.com/chapter/10.1007/978-3-662-49635-0_10). Principles of Security and Trust (POST), 2016. 

## Installation

###### Dependancies

- [Java](https://java.com/en/download/)  (supported version: 1.8 or higher) 
- [Ant](https://ant.apache.org/)

###### Optional Dependancies

- [Maude](http://maude.cs.illinois.edu/w/index.php?title=The_Maude_System) (supported version: 2.7.1)
- [AKISS](https://github.com/akiss/akiss) 
###### Build

Obtain the source code `git clone https://github.com/bauer-matthews/SPAN.git`. 


## Usage

`span [options]`

- `-akiss <location>` :  path to AKISS engine
- `-attackTree`       :  print the attack tree
- `-debug`            :  print debugging information
- `-dot <file>`       :  output attack tree to dot file
- `-glpk <location>`  :  path to glpk engine
- `-help`             :  print this message
- `-kiss <location>`  :  path to KISS engine
- `-maude <location>` :  path to maude engine
- `-maxAttack`        :  find the maximum attack probability
- `-protocol <file>`  :  user specified protocol file
- `-trace`            :  print path exploration


