H=../0.5

cp $H/*.md _includes
cp -rvf $H/doc/img .
for j in . tutorial reference ; do 
mkdir $j
for i in $H/doc/$j/*.md; do 
bb=$(basename $i)
b=$j/$bb
bb=${bb%%.md}
bb=$(echo "$bb" | sed 's/.*/\u&/')
( echo "---" ;\
echo "layout: page" ;\
echo "title: $bb" ;\
echo "---" ;\
cat $i ) | sed -e 's!\.md!\.html!g' | perl -pe 's!\.?\./img/!/img/!g'  >$b
echo $b
done ; done
