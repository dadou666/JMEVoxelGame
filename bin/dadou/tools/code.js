for(var x=0;x <7;x++) {
	for(var y=0;y <7;y++) {
		for(var z=0;z <7;z++) {
			if (x==0 || x==6 ) {
				if (y==1 || y ==5 || z==1 || z==5) {
				$.cube(x,y,z) }
			}
			if (y==0 || y==6 ) {
				if (x==1 || x ==5 || z==1 || z==5) {
				$.cube(x,y,z) }
			}
			if (z==0 || z==6 ) {
				if (y==1 || y ==5 || x==1 || x==5) {
				$.cube(x,y,z) }
			}
			
		}
	}
	
}
