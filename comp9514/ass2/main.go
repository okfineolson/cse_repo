package main

import(
	"fmt"
	"time"
	"io/ioutil"
)

var matches int
var query = "sent"
func main(){

	start:= time.Now()
	search("/")
	fmt.Println(matches,"matches")
	fmt.Println(time.Since(start))
}
func search(path string) {
	files, err := ioutil.ReadDir(path)

	if err == nil{
		for _,file:= range files {

			name := file.Name()
			if name == query {
				matches++
			}
			if file.IsDir(){
				search(path + name + "/")
			}
		}
	}
}
