wget --keep-session-cookies --save-cookies cookies.txt bencarle.com/chess/newgame
TOKEN=`awk '/csrftoken/ {print $7}' cookies.txt`
POST="csrfmiddlewaretoken=$TOKEN&whiteteam=1&blackteam=2"
#echo "$TOKEN"
#OUTPUT=`wget -O- --load-cookies cookies.txt --keep-session-cookies --save-cookies cookies.txt --post-data=$POST bencarle.com/chess/newgame`
LINE=`wget -O- --load-cookies cookies.txt --keep-session-cookies --save-cookies cookies.txt --post-data=$POST bencarle.com/chess/newgame | awk '/startgame/{print $4}'`
ID=`echo $LINE | awk 'BEGIN {FS="/" } ; {print $4}'`
ID=`echo "${ID%?}"`
echo "http://www.bencarle.com/chess/display/$ID"