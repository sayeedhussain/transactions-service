FROM gradle:8.7-jdk21 AS build

VOLUME ["/app"]
WORKDIR /app

# Fix for CVE-2022-24765 (https://github.blog/2022-04-12-git-security-vulnerability-announced/)
RUN git config --global --add safe.directory /app

ENTRYPOINT ["gradle"]

CMD ["bootRun", "--debug"]
