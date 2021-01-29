import React, {useEffect, useState, useMemo } from "react";
import { TableHeader, Pagination, Search } from "../components/DataTable";
const DataTable = () => {
    const [comments, setComments] = useState([]);
    const [totalItems, setTotalItems] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [search, setSearch] = useState("");
    const [sorting, setSorting] = useState({ field: "", order: "" });
    const ITEMS_PER_PAGE = 3;
    const headers = [
        { name: "ID", field: "id", sortable: true },
        { name: "Build ID" , field: "buildNumber", sortable: true },
        { name: "Time", field: "buildTime", sortable: true },
        { name: "Build status", field: "buildStatus", sortable: false },
        { name: "Artifacts", field: "link", sortable: false },
        { name: "Deployment status", field: "dstatus", sortable: false}
    ];
    useEffect(() => {
        const getData = () => {
            fetch("http://localhost:9090/buildInfo")
                .then(response => response.json())
                .then(json => {
                    setComments(json);
                });
        };
        getData();
    }, []);
    const commentsData = useMemo(() => {
        let computedComments = comments;
        if (search) {
            computedComments = computedComments.filter(
                comment =>
                    comment.buildStatus.toLowerCase().includes(search.toLowerCase()) ||
                    comment.dstatus.toLowerCase().includes(search.toLowerCase())
            );
        }
        setTotalItems(computedComments.length);
        //Sorting comments
        if (sorting.field) {
            const reversed = sorting.order === "asc" ? 1 : -1;
            computedComments = computedComments.sort(
                (a, b) =>
                    reversed * a[sorting.field].toString().localeCompare(b[sorting.field])
            );
        }
        //Current Page slice
        return computedComments.slice(
            (currentPage - 1) * ITEMS_PER_PAGE,
            (currentPage - 1) * ITEMS_PER_PAGE + ITEMS_PER_PAGE
        );
    }, [comments, currentPage, search, sorting]);
    return (
        <>
            <div className="row w-100">
                <div className="col mb-3 col-12 text-center">
                    <div className="row" style={{float:"right"}} >
                        <div className="col-md-6" >
                            <Search
                                onSearch={value => {
                                    setSearch(value);
                                    setCurrentPage(1);
                                }}
                            />
                        </div>
                    </div>
                    <table className="table table-striped">
                        <TableHeader
                            headers={headers}
                            onSorting={(field, order) =>
                                setSorting({ field, order })
                            }
                        />
                        <tbody>
                            {commentsData.map(comment => (
                                <tr>
                                    <th scope="row" key={comment.id}>
                                        {comment.id}
                                    </th>
                                    <th>{comment.buildNumber}</th>
                                    <td>{comment.buildTime}</td>
                                    <td>{comment.buildStatus}</td>
                                    <td>{comment.link}</td>
                                    <td>{comment.dstatus}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <div className="row" style={{float:"right"}}>
                        <div className="col-md-6">
                            <Pagination
                                total={totalItems}
                                itemsPerPage={ITEMS_PER_PAGE}
                                currentPage={currentPage}
                                onPageChange={page => setCurrentPage(page)}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};
export default DataTable;