#!/bin/bash

# Usage: scripts/predict.sh path/to/saved/model.xml [{data-encoding} path/to/unlabeled/data.csv...]
# Outputs tab-separated predictions for new instances, using the given model. (instance number, prediction, text)
# If no new data file is given, instances are read from the standard input.
# Common tab encodings are UTF-8, windows-1252, and MacRoman.
# (Make sure that the text columns and any columns used as features 
#  have the same names in the new data as they did in the training set.)

echo "lightside/scripts/predict.sh is executing"

MAXHEAP="4g"
OS_ARGS=""
# OTHER_ARGS="-XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -Djava.awt.headless=true"
OTHER_ARGS="-Djava.awt.headless=true"

MAIN_CLASS="edu.cmu.side.recipe.Predictor"

# CLASSPATH="bin:lib/*:lib/xstream/*:wekafiles/packages/chiSquaredAttributeEval/chiSquaredAttributeEval.jar:wekafiles/packages/bayesianLogisticRegression/bayesianLogisticRegression.jar:wekafiles/packages/LibLINEAR/lib/liblinear-java-1.96-SNAPSHOT.jar:wekafiles/packages/LibLINEAR/LibLINEAR.jar:wekafiles/packages/LibSVM/lib/libsvm.jar:wekafiles/packages/LibSVM/LibSVM.jar:plugins/genesis.jar"

CLASSPATH="/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/bin:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/lib/*:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/lib/xstream/*:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/chiSquaredAttributeEval/chiSquaredAttributeEval.jar:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/bayesianLogisticRegression/bayesianLogisticRegression.jar:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/LibLINEAR/lib/liblinear-java-1.96-SNAPSHOT.jar:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/LibLINEAR/LibLINEAR.jar:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/LibSVM/lib/libsvm.jar:/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/wekafiles/packages/LibSVM/LibSVM.jar:plugins/genesis.jar"
     
# echo "\$CLASSPATH: ..."       
# echo $CLASSPATH        
# echo " "
# echo "Current path: ..."
# echo $PWD  -- [result was '/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside']
echo "Current path: ..."
echo $PWD
        
java $OS_ARGS -Xmx$MAXHEAP $OTHER_ARGS -classpath $CLASSPATH $MAIN_CLASS "$@"
