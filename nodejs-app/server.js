'use strict';

var express = require('express');
var app = express();
var server = require('http').createServer(app);
var port = process.env.PORT || 8080;
var globalres;

server.listen(port, function () {
  console.log('Server listening at port %d', port);
});


var fs= require('fs');
var chunks=[];
var path='audio.amr';

app.get('/', function (req, res) {
    res.send('Hello world!');
});

app.get('/audio',function(req,res){
    res.sendFile(__dirname+'/audio.amr');
});
app.post('/upload',function(req,res){
     chunks=[];
     req.on('data',function(chunk){
     	chunks.push(chunk);
     
     });
     
     req.on('end',function(){
     	var data=Buffer.concat(chunks);
	fs.writeFile(path,data,'binary',function(err){
		if(err)
			console.log('couldnt make file'+err);
		else{
			console.log("Audio Recieved:");
			console.log(data);
		}
	});
     });
});


