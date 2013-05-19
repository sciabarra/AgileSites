H=../0.5

cp $H/*.md _includes
for j in . tutorial reference ; do 
mkdir $j
for i in $H/doc/$j/*.md; do 
bb=$(basename $i)
bb=${bb%%.md}
echo $bb
if test "$bb" = "index"; then bb=$j ; fi
b=$j/$bb
( echo "---" ;\
echo "layout: page" ;\
echo "title: $bb" ;\
echo "---" ;\
cat $i ) | sed -e 's!\.md!\.html!g'  >$b
echo $b
done ; done

