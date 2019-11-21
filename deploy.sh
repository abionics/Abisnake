heroku ps:scale web=0	# stop snake
git add .
git commit -m "run.sh"
git push heroku master
heroku ps:scale web=1
