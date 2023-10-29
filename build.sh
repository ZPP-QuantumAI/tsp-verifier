app_name="tsp-verifier"
file_path="version.txt"
release="$1"

if [ -z "$release" ]; then
    release="patch"
fi

current_version=$(grep -o '[0-9]*\.[0-9]*\.[0-9]*' "$file_path")

IFS='.' 
read -r major minor patch <<< "$current_version"

if [ "$release" == "major" ]; then
  ((major++))
  ((minor=0))
  ((patch=0))
fi

if [ "$release" == "minor" ]; then
  ((minor++))
  ((patch=0))
fi

if [ "$release" == "patch" ]; then
  ((patch++))
fi

new_version="$major.$minor.$patch"
echo "The new release version: $new_version"

while true; do
    read -p "Continue? [Y/n] " yn
    case $yn in
        [Yy]* ) break;;
        [Nn]* ) exit 1;;
        * ) echo "Please answer y or n";;
    esac
done

docker build -t "$app_name:$new_version" "."

if [ "$?" -eq 1 ]; then
    exit 1
fi

echo "Released version $new_version!"
echo "v$new_version" > version.txt
