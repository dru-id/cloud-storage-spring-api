#!/usr/bin/env bash

usage() {
    echo "usage: $1 <release_version> <new_deployment_version (this script will append '-SNAPSHOT' automatically)> [maven params]"
    echo "   eg: $1 19.08.1-Nitrogenium 1.0.0-Oxygenium"
    echo "   eg: $1 19.08.1-Nitrogenium 1.0.0-Oxygenium -e -X"
    exit -1
}
if [ "$#" -lt 2 ]; then
    usage $0
fi

RELEASE="$1"; shift
SNAPSHOT="$1"; shift
BRANCH=$(git rev-parse --abbrev-ref HEAD)
DEF_BRANCH="master"
KEY=""

echo "You are generating a release from branch $BRANCH"

#if [[ "$BRANCH" != $DEF_BRANCH ]]; then
#  read -p "You will release from non $DEF_BRANCH branch $BRANCH. are you sure? (y/N)" -n1 -s KEY
#  echo $KEY
#  exit 1;
#fi

echo "pulling changes..." && \
bash ./update.sh && \
echo "Preparing release $RELEASE (next devel will be $SNAPSHOT)..." && \
mvn $@ --batch-mode -Dtag=$RELEASE -DreleaseVersion=$RELEASE -DdevelopmentVersion=$SNAPSHOT-SNAPSHOT clean compile release:prepare && \
echo "Performing release $RELEASE ..." && \
mvn $@ --batch-mode -Dtag=$RELEASE -DreleaseVersion=$RELEASE -DdevelopmentVersion=$SNAPSHOT-SNAPSHOT release:perform && \
echo "Release $RELEASE is done!"
