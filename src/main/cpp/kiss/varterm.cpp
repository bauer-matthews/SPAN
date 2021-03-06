#include "varterm.h"
#include "funterm.h"
#include "namterm.h"
#include "helper.h"
#include "factories.h"

using namespace std;

VarTerm::VarTerm(Variable *variable)
{
  this->variable = variable;
}

vector<Variable *> VarTerm::computeVars()
{
  vector<Variable *> result;
  result.push_back(variable);
  return result;
}

vector<Name *> VarTerm::names()
{
  vector<Name *> result;
  return result;
}

Term *VarTerm::computeSubstitution(Substitution &subst, map<Term *, Term *> &)
{
  if (subst.inDomain(variable)) {
    return subst.image(variable);
  } else {
    return this;
  }
}

string VarTerm::toString()
{
  return variable->name;
}

bool VarTerm::unifyWith(Term *t, Substitution &subst)
{
  logmgu("VarTerm::unifyWith", this, t, subst);
  return t->unifyWithVarTerm(this, subst);
}

bool VarTerm::unifyWithVarTerm(VarTerm *t, Substitution &subst)
{
  logmgu("VarTerm::unifyWithVarTerm", this, t, subst);
  if (this == t) {
    return true;
  }
  if (subst.inDomain(this->variable)) {
    if (subst.inDomain(t->variable)) {
      return subst.image(this->variable)->unifyWith(subst.image(t->variable), subst);
    } else {
      return subst.image(this->variable)->unifyWithVarTerm(t, subst);
    }
  } else {
    if (subst.inDomain(t->variable)) {
      return subst.image(t->variable)->unifyWithVarTerm(this, subst);
    } else {
      subst.force(this->variable, t);
    }
    return true;
  }
}

bool VarTerm::unifyWithFunTerm(FunTerm *t, Substitution &subst)
{
  logmgu("VarTerm::unifyWithFunTerm", this, t, subst);
  vector<Variable *> ocVars = t->vars();

  if (subst.inDomain(this->variable)) {
    return subst.image(this->variable)->unifyWithFunTerm(t, subst);
  } else if (contains(ocVars, this->variable)) {
    return false;
  } {
    subst.force(this->variable, t);
    return true;
  }
}

bool VarTerm::unifyWithNamTerm(NamTerm *t, Substitution &subst)
{
  logmgu("VarTerm::unifyWithNamTerm", this, t, subst);
  if (subst.inDomain(this->variable)) {
    return subst.image(this->variable)->unifyWithNamTerm(t, subst);
  } else {
    subst.force(this->variable, t);
    return true;
  }
}

bool VarTerm::isVariable()
{
  return true;
}

vector<pair<Term *, Term *> > VarTerm::split()
{
  vector<pair<Term *, Term *> > result;
  result.push_back(make_pair(getVarTerm(getVariable("\\_")), this));
  return result;
}

Term *VarTerm::generatedBy(KnowledgeBase &knowledgeBase, map<Term *, Term *> &cache)
{
  if (!contains(cache, dynamic_cast<Term *>(this))) {
    cache[this] = this;
  }
  return cache[this];
}

bool VarTerm::computeIsNormalized(RewriteSystem &, map<Term *, bool> &)
{
  return true;
}

Term *VarTerm::computeNormalize(RewriteSystem &, map<Term *, Term *> &)
{
  return this;
}

bool VarTerm::computeIsInstanceOf(Term *t, Substitution &s, map<pair<Term *, Term *>, bool> &cache)
{
  return t->computeIsGeneralizationOf(this, s, cache);
}

bool VarTerm::computeIsGeneralizationOf(NamTerm *t, Substitution &s, map<pair<Term *, Term *>, bool> &cache)
{
  if (!contains(cache, make_pair((Term *)t, (Term *)this))) {
    if (contains(s, this->variable)) {
      cache[make_pair(t, this)] = s.image(this->variable) == t;
    } else {
      s.add(this->variable, t);
      cache[make_pair(t, this)] = true;
    }
  }
  return cache[make_pair(t, this)];
}

bool VarTerm::computeIsGeneralizationOf(VarTerm *t, Substitution &s, map<pair<Term *, Term *>, bool> &cache)
{
  if (!contains(cache, make_pair((Term *)t, (Term *)this))) {
    if (contains(s, this->variable)) {
      cache[make_pair(t, this)] = s.image(this->variable) == t;
    } else {
      s.add(this->variable, t);
      cache[make_pair(t, this)] = true;
    }
  }
  return cache[make_pair(t, this)];
}

bool VarTerm::computeIsGeneralizationOf(FunTerm *t, Substitution &s, map<pair<Term *, Term *>, bool> &cache)
{
  if (!contains(cache, make_pair((Term *)t, (Term *)this))) {
    if (contains(s, this->variable)) {
      cache[make_pair(t, this)] = s.image(this->variable) == t;
    } else {
      s.add(this->variable, t);
      cache[make_pair(t, this)] = true;
    }
  }
  return cache[make_pair(t, this)];
}

Term *VarTerm::computeApplyFrame(Frame *phi, map<Term *, Term *> &)
{
  return this;
}

int VarTerm::computeDagSize(map<Term *, int> &cache)
{
  if (contains(cache, (Term *)this)) {
    return 0;
  } else {
    cache[this] = 1;
    return 1;
  }
}
