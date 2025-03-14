# springboot-basic-profiles-docker
A basic functional example of spring boot hello world with profiles ready to be dockerized

docker buildx build -t your_user/your_image_name:latest --platform linux/amd64 .

Using existing docker image: docker push pierinho13/hello-world-profiles:latest