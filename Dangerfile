message("message-1")
message("message-2")
message("message-3")
message("message", file: "Dangerfile", line: 4)

warn("warm-1")
warn("warm-2")
warn("warm-3")
warn("warn", file: "Dangerfile", line: 9)

fail("fail-1")
fail("fail-2")
fail("fail-3")
fail("fail", file: "Dangerfile", line: 14)

markdown("## h2")
markdown("### h3")

table_header = "|テーブル|\n| --- |\n"
table_body = ["aaa", "bbb", "ccc"].map { |s| "| #{s} |\n"}.join
markdown(table_header + table_body)
