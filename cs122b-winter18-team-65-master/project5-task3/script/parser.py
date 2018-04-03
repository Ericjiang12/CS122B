count = 0
tj = 0
ts = 0
with open("project5_log_file.txt") as infile:
    for line in infile:
	line = line.split(";")
	for i in line:
		if not i:
			continue
		if i.split(":")[0] == "tj":
			tj += float(i.split(":")[1])
		else:	
			ts += float(i.split(":")[1])
			count += 1

print("tj: " + str(tj/int(count)))
print("ts: " + str(ts/int(count)))
        
